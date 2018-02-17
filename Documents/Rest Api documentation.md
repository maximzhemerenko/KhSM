Version 1.0

Endpoint: {server_url}/api/

# Methods

## Login
POST sessions

Request: [CreateSessionRequest](#createsessionrequest)

Response: [Session](#session)

## Register
POST users

Request: [CreateUserRequest](#createuserrequest)

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
  Gender: [String\<Gender\>](#stringgender)>
}

## String\<Gender\>
- "Female"
- "Male"
