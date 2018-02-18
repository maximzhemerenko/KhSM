# Information

Version: 1.0

Endpoint: {server_url}/api/

Authentication: Use `Authorization` http header to authorize request.

# Methods

## Login
POST sessions

Request: [CreateSessionRequest](#createsessionrequest)

Response: [Session](#session)

## Register
POST users

Request: [CreateUserRequest](#createuserrequest)

Response: [Session](#session)

## Get user
GET users/{id}

Response: [User](#user)

## Update user
PUT users/{id}

Request: [User](#user)

Response: [User](#user)

## Get user records
Get users/{id}/records

Response: List<[DisciplineRecord](#disciplinerecord)>

## Get user results
Get users/{id}/results

Response: Array<[DisciplineResults](#disciplineresults)>

## Get meetings
GET meetings

Response: Array\<[Meeting](#meeting)\>

## Get meeting
GET meetings/{id}

Response: [Meeting](#meeting)

## Get meeting results
GET meetings/{id}/results

Response: Array<[DisciplineResults](#disciplineresults)>

# Entities

## CreateSessionRequest 
- email: String
- password: String

## Session
- email: String,
- userId: Integer
- token: String
- user: [User](#user)

## Role
- roleId: Integer
- name: String
- key: String

## CreateUserRequest
- user: [User](#user)
- password: String

## User
- id: Integer
- firstName: String
- lastName: String
- email: String
- gender: [String\<Gender\>](#stringgender)
- roles: Array\<[Role](#role)\>
- wcaId: String
- city: String
- birth_date: Date
- phoneNumber: String

## String\<Gender\>
- "female"
- "male"

## Meeting
- id: Integer
- number: Integer
- date: Date

## Discipline
- id: Integer
- name: String
- description: String
- attempsCount: Integer

## DisciplineResults
- discipline: [Discipline](#discipline)
- results: Array\<[Result](#result)\>

## DisciplineRecord
- discipline: [Discipline](#discipline)
- bestTime: [Result](#result)
- bestOverageTime: [Result](#result)

## Result
- id: Integer
- meetings: [Meeting](#meeting)
- user: [User](#user)
- average: Nullable\<Float\>
- attempts: Array\<Nullable\<Float\>\>
