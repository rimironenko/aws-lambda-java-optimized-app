package com.home.amazon.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
public class DynamoDbWriteHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient = DependencyFactory.dynamoDbEnhancedClient();
    private static final String TABLE_NAME = DependencyFactory.tableName();
    private static final TableSchema<DataModel> TABLE_SCHEMA = TableSchema.fromBean(DataModel.class);
    private final DynamoDbTable<DataModel> itemTable = dynamoDbEnhancedClient.table(TABLE_NAME, TABLE_SCHEMA);

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        DataModel dataModel = new DataModel();
        dataModel.setId(context.getAwsRequestId());
        itemTable.putItem(dataModel);
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200);
    }

}
