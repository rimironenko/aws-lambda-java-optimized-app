package com.home.amazon.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

/**
 * Lambda function entry point. You can change to use other pojo type or implement
 * a different RequestHandler.
 *
 * @see <a href=https://docs.aws.amazon.com/lambda/latest/dg/java-handler.html>Lambda Java Handler</a> for more information
 */
public class GetItemFunction implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final DynamoDbEnhancedClient DYNAMO_DB_CLIENT = DependencyFactory.dynamoDbEnhancedClient();
    private static final String TABLE_NAME = DependencyFactory.tableName();
    private static final TableSchema<DataModel> TABLE_SCHEMA = TableSchema.fromBean(DataModel.class);

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        String response = "";
        DynamoDbTable<DataModel> booksTable = DYNAMO_DB_CLIENT.table(TABLE_NAME, TABLE_SCHEMA);
        String itemPartitionKey = request.getPathParameters().get(DataModel.PARTITION_KEY);
        if (itemPartitionKey != null) {
            DataModel item = booksTable.getItem(Key.builder().partitionValue(itemPartitionKey).build());
            if (item != null) {
                response = item.toString();
            }
        }

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody(response);
    }

}