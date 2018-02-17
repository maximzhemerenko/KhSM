Version 1.0

# Methods

Endpoint: {server_url}/api/

## Login
POST sessions

Request: [CreateSessionRequest](#CreateSessionRequest)

Response: [Session](#Session)

# Entities

## CreateSessionRequest 
{
  "email": String,
  "password": String
}

## Session
{
  "email": String,
  "userId": Integer,
  "roles": Array<[Role](#Role)>
  "token": String
}

## Role
{
  "roleId": Integer,
  "name": String,
  "key": String
}
