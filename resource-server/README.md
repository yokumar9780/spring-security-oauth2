# OAuth 2.0 Resource Server

This sample demonstrates integrating Resource Server with the Spring Authorization Server however can be modified to
integrate with any other Authorization Server.

### Testing with Authorization Servers

Any Authorization Server must support JWTs that either use the "scope" or "scp" attribute.
To change the sample to point at your Authorization Server, simply find this property in the `application.yml`:

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          jwk-set-uri: http://localhost:9001/oauth2/jwks
```

And change the property to your Authorization Server's JWK set endpoint:

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          jwk-set-uri: https://dev-123456.oktapreview.com/oauth2/default/v1/keys
```

And then you can run the app the same as before:

```bash
mvn springboot:start
```

Make sure to obtain valid tokens from your Authorization Server in order to play with the sample Resource Server.
To use the `/` endpoint, any valid token from your Authorization Server will do.
To use the `/message` endpoint, the token should have the `message:read` scope.

To run the tests, do:

```bash
mvn test
```

Or import the project into your IDE and run `ResourceServerControllerTest` from there.

#### Test using authorization server

```bash
curl -X POST messaging-client:secret@localhost:9001/oauth2/token -d "grant_type=client_credentials" -d "scope=message:read"
```

This returns something like the following:

```json
{
  "access_token": "eyJraWQiOiI4YWY4Zjc2Zi0zMTdkLTQxZmYtYWY5Yi1hZjg5NDg4ODM5YzciLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJtZXNzYWdpbmctY2xpZW50IiwiYXVkIjoibWVzc2FnaW5nLWNsaWVudCIsIm5iZiI6MTYyNzMzNDQ1MCwic2NvcGUiOlsibWVzc2FnZTpyZWFkIl0sImlzcyI6Imh0dHA6XC9cL2xvY2FsaG9zdDo5MDAwIiwiZXhwIjoxNjI3MzM0NzUwLCJpYXQiOjE2MjczMzQ0NTAsImp0aSI6IjBiYjYwZjhkLWIzNjItNDk0MC05MGRmLWZhZDg4N2Q1Yzg1ZSJ9.O8dI67B_feRjOn6pJi5ctPJmUJCNpV77SC4OiWqmpa5UHvf4Ud6L6EFe9LKuPIRrEWi8rMdCdMBOPKQMXvxLoI3LMUPf7Yj973uvZN0E988MsKwhGwxyaa_Wam8wFlk8aQlN8SbW3cKdeH-nKloNMdwjfspovefX521mxouaMjmyXdIFrM5WZ15GZK69NIniACSatE-pc9TAjKYBDbC65jVt_zHEvDQbEkZulF2bjrGOZC8C3IbJWnlKgkcshrY44TtrGPyCp2gIS0TSUUsG00iSBBC8E8zPU-YdfaP8gB9_FwUwK9zfy_hU2Ykf2aU3eulpGDVLn2rCwFeK86Rw1w",
  "expires_in": 299,
  "scope": "message:read",
  "token_type": "Bearer"
}
```

Then, export the access token from the response:

```bash
export TOKEN=...
```

Then issue the following request:

```bash
curl -H "Authorization: Bearer $TOKEN" localhost:8080
```

Which will respond with the phrase:

```
Hello, messaging-client!
```

where `messaging-client` is the value of the `sub` field in the JWT returned by the Authorization Server.

Or this to make a GET request to /message:

```bash
curl -H "Authorization: Bearer $TOKEN" localhost:8080/message
```

Will respond with:

```bash
secret message
```

In order to make a POST request to /message, you can use the following request:

```bash
curl -X POST messaging-client:secret@localhost:9000/oauth2/token -d "grant_type=client_credentials" -d "scope=message:write"
```

Then, export the access token from the response:

```bash
export TOKEN=...
```

Then issue the following request:

```bash
curl -H "Authorization: Bearer $TOKEN" -d "my message" localhost:8080/message
```

Which will respond with:

```bash
Message was created. Content: my message
```

