Version 1.0

# Methods

Endpoint: {server_url}/api/

## Login
POST sessions

Request: [CreateSessionRequest](#createsessionrequest)

Response: [Session](#session)

## Register
POST users

Query:
- createSession: Nullable\<Boolean\>

Request: [CreateUserRequest](#createuserrequest)

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

## CreateUserRequest
{
  "user": [User](#user),
  "password": String
}

## User
{
  "id": Integer
  "firstName": String,
  "lastName": String,
  "email": String,
  Gender: [String\<Gender\>](string-gender)>
}

## String\<Gender\>
- "Female"
- "Male"
