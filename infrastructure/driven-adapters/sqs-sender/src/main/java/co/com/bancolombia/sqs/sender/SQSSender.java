package co.com.bancolombia.sqs.sender;

import co.com.bancolombia.model.loanpetition.UserInfoToLambda;
import co.com.bancolombia.model.loanpetition.gateways.SqsGateway;
import co.com.bancolombia.model.response.LoanPetitionInformation;
import co.com.bancolombia.sqs.sender.config.SQSSenderProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
public class SQSSender implements SqsGateway {
    private final SQSSenderProperties properties;
    private final SqsAsyncClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public Mono<Boolean> sendMessageToUser(UserInfoToLambda info) {
        String body;
        try {
            body = mapper.writeValueAsString(info);
        } catch (JsonProcessingException e) {
            return Mono.error(new IllegalArgumentException("Serializer Error UserInfoToLambda", e));
        }
        String groupId = properties.fifo()
                ? properties.messageGroupPrefix() + normalizeForGroup(info.getEmail())
                : null;
        String dedupeId = properties.fifo() ? UUID.randomUUID().toString() : null;
        SendMessageRequest req = buildRequest(body, groupId, dedupeId, properties.queueUrl());
        return Mono.fromFuture(client.sendMessage(req))
                .doOnNext(r -> log.info("SQS messageId={} email={} state={}",
                        r.messageId(), info.getEmail(), info.getState()))
                .map(r -> true);
    }

    public Mono<Boolean> sendInfoToLambdaDebtCapacity(List<LoanPetitionInformation> loanPetitions, BigDecimal salary) {
        if (loanPetitions == null || loanPetitions.isEmpty()) {
            log.info("sendInfoToLambdaDebtCapacity: empty list");
            return Mono.just(false);
        }
        loanPetitions.forEach(loanPetition -> loanPetition.setSalary(salary));
        String body;
        try {
            body = mapper.writeValueAsString(loanPetitions);
        } catch (JsonProcessingException e) {
            return Mono.error(new IllegalArgumentException("Serializer Error LoanPetition list", e));
        }
        String groupId = properties.fifo()
                ? properties.messageGroupPrefix() + "debtCapacity"
                : null;
        String dedupeId = properties.fifo() ? UUID.randomUUID().toString() : null;
        SendMessageRequest req = buildRequest(body, groupId, dedupeId, properties.queueUrl2());
        return Mono.fromFuture(client.sendMessage(req))
                .doOnNext(r -> log.info("SQS sendInfoToLambdaDebtCapacity OK messageId={} size={} petitions={}",
                        r.messageId(), body.length(), loanPetitions.size()))
                .map(r -> true);
    }

    private SendMessageRequest buildRequest(String body, String messageGroupId, String dedupeId, String url) {
        SendMessageRequest.Builder builder = SendMessageRequest.builder()
                .queueUrl(url)
                .messageBody(body);
        if (properties.fifo()) {
            builder = builder.messageGroupId(messageGroupId).messageDeduplicationId(dedupeId);
        }
        return builder.build();
    }

    private static String normalizeForGroup(String key) {
        return key.replaceAll("[^A-Za-z0-9._-]", "_");
    }

}
