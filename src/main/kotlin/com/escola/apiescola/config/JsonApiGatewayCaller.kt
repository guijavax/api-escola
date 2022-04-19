package com.escola.apiescola.config

import com.amazonaws.*
import com.amazonaws.auth.AWS4Signer
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.http.ExecutionContext
import com.amazonaws.http.HttpMethodName
import com.amazonaws.http.JsonErrorResponseHandler
import com.amazonaws.http.JsonResponseHandler
import com.amazonaws.internal.AmazonWebServiceRequestAdapter
import com.amazonaws.internal.auth.DefaultSignerProvider
import com.amazonaws.protocol.json.JsonOperationMetadata
import com.amazonaws.protocol.json.SdkStructuredPlainJsonFactory
import com.amazonaws.transform.JsonErrorUnmarshaller
import com.amazonaws.transform.JsonUnmarshallerContext
import com.amazonaws.transform.Unmarshaller
import com.escola.apiescola.service.ApiGatewayResponse
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.InputStream
import java.net.URI
import java.util.*
import javax.annotation.PostConstruct


@Service
class JsonApiGatewayCaller : AmazonWebServiceClient(ClientConfiguration()) {

    @Value(value = "\${AWS_IAM_ACCESS_KEY}")
    lateinit var accessKey : String

    @Value(value = "\${AWS_IAM_SECRET_ACCESS_KEY}")
    lateinit var secretAccessKey : String

    @Value(value = "\${AWS_REGION}")
    lateinit var region : String

    @Value(value = "\${AWS_URI}")
    lateinit var uriBase : String

    @Value(value = "\${AWS_SERVICE_KEY}")
    lateinit var serviceKey : String

//    lateinit var defaultErrorUnmarshaller : JsonErrorUnmarshaller

    lateinit var endpointURI : URI

   lateinit var credentials : AWSStaticCredentialsProvider

    lateinit var errorHandler: JsonErrorResponseHandler

    lateinit var jsonResponseHandler : JsonResponseHandler<ApiGatewayResponse>

    lateinit var signer: AWS4Signer

    @PostConstruct
    fun init() {
        endpointURI  = URI.create(uriBase)

        credentials = AWSStaticCredentialsProvider(BasicAWSCredentials(accessKey, secretAccessKey))

        signer = AWS4Signer().apply {
            serviceName = serviceKey
            regionName = region
        }
        val metadata = JsonOperationMetadata().withHasStreamingSuccessResponse(false).withPayloadJson(false)
        val responseUnmarshaller: Unmarshaller<ApiGatewayResponse, JsonUnmarshallerContext> =
            Unmarshaller<ApiGatewayResponse, JsonUnmarshallerContext> { ApiGatewayResponse(it.getHttpResponse()) }

        jsonResponseHandler = SdkStructuredPlainJsonFactory.SDK_JSON_FACTORY.createResponseHandler(metadata, responseUnmarshaller)

        val  defaultErrorUnmarshaller : JsonErrorUnmarshaller = object : JsonErrorUnmarshaller(
            AmazonServiceException::class.java, null
        ) {
            @Throws(Exception::class)
            override fun unmarshall(jsonContent: JsonNode): AmazonServiceException? {
                return AmazonServiceException(jsonContent.toString())
            }
        }

        errorHandler = SdkStructuredPlainJsonFactory.SDK_JSON_FACTORY.createErrorResponseHandler(Collections.singletonList(defaultErrorUnmarshaller), null)

    }

    fun execute(method : HttpMethodName, resourcePath : String, content : InputStream) : ApiGatewayResponse {
        val executionContext = createExecutionContext()
        val request = prepareRequest(method, resourcePath, content)
        val requestConfig = AmazonWebServiceRequestAdapter(request.originalRequest)
        return client.execute(request, jsonResponseHandler, errorHandler, executionContext, requestConfig).awsResponse
    }

    internal fun prepareRequest(method : HttpMethodName, resourcePathValue : String, contentValue : InputStream) : DefaultRequest<String> {
        return DefaultRequest<String>(serviceKey).apply {
            httpMethod = HttpMethodName.POST
            content = contentValue
            endpoint = endpointURI
            resourcePath = resourcePathValue
            headers = Collections.singletonMap("Content-type", "application/json")

        }
    }

    private fun createExecutionContext(): ExecutionContext? {
        return ExecutionContext.builder().withSignerProvider(
            DefaultSignerProvider(this, signer)
        ).build().apply {
            credentialsProvider = credentials
        }

    }

}