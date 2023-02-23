Dream Games Case Study
======================

These are example requests and responses for Row Match service running at [http://localhost:8080](http://localhost:8080)

USER API
--------

### CreateUserRequest

This endpoint creates a user with 5000 coins which starts from level 1. Created users by this endpoint have no team information. The endpoint returns unique id, level and coin information of the created user.

<br>

*Request*

    POST /api/v1/user HTTP/1.1
    Host: localhost:8080
    Content-Type: application/x-www-form-urlencoded

<br>

*Response*


    HTTP/1.1 200 OK
    Content-Type: application/json
    Content-Length: 65
    
    {
      "id" : 394,
      "level" : 1,
      "coin" : 5000,
      "team" : null
    }

<br>

### UpdateLevelRequest

This endpoints receives user id information as a path variable and updates related user. Level of the user will be increased by 1 and coins of the user will increased by 25 as a level up reward. The endpoint returns unique id, level and coin information of the updated user.

<br>

*Possible Errors:*

| **Status Code** | **Error Message** |
|---|---|
| 404 | User Not Found |

<br>

*Request*

    PUT /api/v1/user/395 HTTP/1.1
    Host: localhost:8080
    Content-Type: application/x-www-form-urlencoded

<br>

*Response*

    HTTP/1.1 200 OK
    Content-Type: application/json
    Content-Length: 65
    
    {
      "id" : 395,
      "level" : 2,
      "coin" : 5025,
      "team" : null
    }

<br>
<br>

TEAM API
--------

### CreateTeamRequest

This endpoints receives user id and team name information by parsing request body. It creates a new team with given name and sets initial member count as 1. It also sets given user’s team information and deduct coins of the user by 5000.

<br>

*Possible Errors:*

| **Status Code** | **Error Message** |
|-----------------| --- |
| 404             | User Not Found |
| 403             | User has to have at least 1000 coins to create a team |
| 403             | User is already a member of another team, leave your team before join to a new team |
| 403             | Given team name is already taken |

<br>

*Request*

    POST /api/v1/team HTTP/1.1
    Content-Type: application/json;charset=UTF-8
    Content-Length: 77
    Host: localhost:8080
    
    {
      "teamName" : "6aa45cb1-f7f0-4e1d-af92-cc1c2c1ac55e",
      "userId" : "398"
    }

<br>

*Response*

    HTTP/1.1 200 OK
    Content-Type: application/json
    Content-Length: 107
    
    {
      "id" : 953,
      "name" : "6aa45cb1-f7f0-4e1d-af92-cc1c2c1ac55e",
      "capacity" : 20,
      "memberCount" : 1
    }

<br>

### JoinTeamRequest

This endpoints receives team id information as path variable and user id information by parsing request body. It sets team information of given user and increase member count of given teams.

<br>

*Possible Errors:*

| **Status Code** | **Error Message** |
| --- | --- |
| 404 | User Not Found |
| 404 | Team Not Found |
| 403 | User has to have at least 1000 coins to create a team |
| 403 | User is already a member of the requested team |
| 403 | User is already a member of another team, leave your team before join to a new team |
| 403 | Team is full |

<br>

*Request*

    PUT /api/v1/team/952/join HTTP/1.1
    Content-Type: application/json;charset=UTF-8
    Content-Length: 22
    Host: localhost:8080
    
    {
      "userId" : "397"
    }

<br>

*Response*

    HTTP/1.1 200 OK
    Content-Type: application/json
    Content-Length: 107
    
    {
      "id" : 952,
      "name" : "0b86e47b-2d29-4e72-a171-277d3c279922",
      "capacity" : 20,
      "memberCount" : 2
    }

<br>

### GetTeams

This endpoint returns random 10 teams which is not full. If there is no 10 available teams, it returns as many available teams as possible. It randomizes returned teams and their order for each request.

Request

    GET /api/v1/team/available HTTP/1.1
    Host: localhost:8080

Response

    HTTP/1.1 200 OK
    Content-Type: application/json
    Content-Length: 1092
    
    [ {
      "id" : 308,
      "name" : "3a68d66a-bc2d-431f-bfce-078d57794b68",
      "capacity" : 20,
      "memberCount" : 1
    }, {
      "id" : 816,
      "name" : "6d2ff34b-ccb1-4a11-b3a1-34a002dff9f5",
      "capacity" : 20,
      "memberCount" : 1
    }, {
      "id" : 506,
      "name" : "db19355d-b2ff-4525-9dc6-991101e83474",
      "capacity" : 20,
      "memberCount" : 1
    }, {
      "id" : 717,
      "name" : "597843af-2c7e-45d2-8d9a-5546c227ef52",
      "capacity" : 20,
      "memberCount" : 1
    }, {
      "id" : 806,
      "name" : "6c10b8c5-e86d-4626-a5f4-64d3d8122a61",
      "capacity" : 20,
      "memberCount" : 1
    }, {
      "id" : 313,
      "name" : "7fc8d6d0-e5c2-405d-8255-6fb781b81fea",
      "capacity" : 20,
      "memberCount" : 1
    }, {
      "id" : 656,
      "name" : "cabf9c4d-44d5-4b3c-9e24-898f4c93efc9",
      "capacity" : 20,
      "memberCount" : 1
    }, {
      "id" : 662,
      "name" : "3f89bc99-c06a-4520-b085-eee8d93ed994",
      "capacity" : 20,
      "memberCount" : 1
    }, {
      "id" : 914,
      "name" : "243a69fb-6469-45d2-8419-f5cfb93ee938",
      "capacity" : 20,
      "memberCount" : 1
    }, {
      "id" : 117,
      "name" : "8bd58be5-57c4-4239-80cf-c61644400bd4",
      "capacity" : 20,
      "memberCount" : 1
    } ]

<br>
