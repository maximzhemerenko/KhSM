Version 1.0

# Methods

Endpoint: {server_url}/api/

## Login
POST sessions

Request: [CreateSessionRequest](#createsessionrequest)

Response: [Session](#session)

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
  "roles": Array<[Role](#role)>
  "token": String
}

## Role
{
  "roleId": Integer,
  "name": String,
  "key": String
}
