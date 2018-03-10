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

GET users/me

Response: [User](#user)

## Update user
PUT users/{id}

PUT users/me

Request: [User](#user)

Response: [User](#user)

## Get user records
GET users/{id}/records

GET users/me/records

Response: Array<[DisciplineRecord](#disciplinerecord)>

## Get user results
GET users/{id}/results

GET users/me/results

Response: Array<[DisciplineResults](#disciplineresults)>

## Insert user result
POST users/{id}/results

POST users/me/results

Request: [Result](#result)

Response: [Result](#result)

## Get meetings
GET meetings

Response: Array\<[Meeting](#meeting)\>

## Get meeting
GET meetings/{id}

Response: [Meeting](#meeting)

## Get meeting results
GET meetings/{id}/results

Response: Array\<[DisciplineResults](#disciplineresults)\>

## Get last meeting
GET meetings/last

Response: [Meeting](#meeting)

## Get disciplines
GET disciplines

Response: Array\<[Discipline](#discipline)\>

## Get rankings
GET rankings

Query:
- type: "average" | "single"
- sort: "ascending" | "descending"
- gender: [String\<Gender\>](#stringgender)

Response: Array\<[DisciplineResults](#disciplineresults)\>

# Entities

## CreateSessionRequest 
- email: String
- password: String

## Session
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
- attemptsCount: Integer

## DisciplineResults
- discipline: [Discipline](#discipline)
- results: Array\<[Result](#result)\>

## DisciplineRecord
- discipline: [Discipline](#discipline)
- bestSingleResult: [Result](#result)
- bestAverageResult: [Result](#result)

## Result
- id: Integer
- meetings: [Meeting](#meeting)
- user: [User](#user)
- average: Nullable\<Float\>
- attempts: Array\<Nullable\<Float\>\>
