package com.home.amazon.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DynamoDbWriteHandlerTest {

    private static final String TEST_TABLE_NAME = "TestTable";

    @Mock
    private DynamoDbEnhancedClient client;
    @Mock
    private DynamoDbTable<DataModel> table;

    @Mock
    private APIGatewayProxyRequestEvent request;

    @Mock
    private Context context;

    @Test
    public void shouldCreateAnItemInTable() {
        try (MockedStatic<DependencyFactory> dependencyFactoryMockedStatic = mockStatic(DependencyFactory.class)) {
            dependencyFactoryMockedStatic.when(DependencyFactory::dynamoDbEnhancedClient).thenReturn(client);
            dependencyFactoryMockedStatic.when(DependencyFactory::tableName).thenReturn(TEST_TABLE_NAME);
            when(client.table(eq(TEST_TABLE_NAME), any(TableSchema.class))).thenReturn(table);
            DynamoDbWriteHandler target = new DynamoDbWriteHandler();
            APIGatewayProxyResponseEvent lambdaResponse = target.handleRequest(request, context);
            verify(table).putItem(any(DataModel.class));
            assertEquals(200, (int) lambdaResponse.getStatusCode());
        }

    }

}