#Social Application

###Running application

This is SpringBoot application using Java 11 and Gradle

To run this application go to project roor folder (`/social`) and invoke:

```
(on Windows)
gradlew bootRun

(on Unix)
./gradlew bootRun
```

If you have no `JAVA_HOME` environment variable setup it can throw error: 

```
ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
```

To set `JAVA_HOME` variable:

```
(on Windows)
set JAVA_HOME="C:\Program Files\Java\jdk-11"

(on Unix)
export JAVA_HOME="path to java"
```

###API description

###Posting messages

To post a message invoke:

```
POST localhost:8080/messages/
```

Example Request Body:
```
{
      "userEmail": "first@example.com",
      "messageContent": "FirstUserMessage1"
}
```

###Reading messages (Wall)

To read user messages invoke:

```
GET localhost:8080/messages/?userEmail=first@example.com`
```

Expected result:

```
[
    {
        "user": {
            "email": "first@example.com",
            "followedUsersEmails": []
        },
        "content": "FirstUserMessage1",
        "createTime": "2020-12-01T12:03:19.2259554"
    }
]
```

If user with given `userEmail` doesn't exists exception is thrown:
```
com.pczyz.social.domain.user.UserNotExistsException: User [first@example.com] not exists
```

###Following users

To follow user invoke:

```
POST localhost:8080/users/follow/?followingEmail=first@example.com&followedEmail=second@example.com
```

If `followingUser` or `followedUser` doesn't exists exception is thrown:
```
com.pczyz.social.domain.user.UserNotExistsException: User [first@example.com] not exists
```

To unfollow user invoke:

```
DELETE localhost:8080/users/follow/?followingEmail=first@example.com&followedEmail=second@example.com
```

If `followingUser` or `followedUser` doesn't exists exception is thrown:
```
com.pczyz.social.domain.user.UserNotExistsException: User [first@example.com] not exists
```

If `followingUser` is not following `followedUser` exception is thrown:
```
com.pczyz.social.domain.user.UserNotFollowingException: User [first@example.com] is not following user [second@example.com]
```

###Reading followed users messages (Timeline)

To read a followed users messages invoke:

```
GET localhost:8080/messages/?userEmail=first@example.com`
```

Expected result:

```
[
    {
        "user": {
            "email": "second@example.com",
            "followedUsersEmails": []
        },
        "content": "SecondUserMessage1",
        "createTime": "2020-12-01T12:15:03.0180958"
    }
]
```

If user with given `userEmail` doesn't exists exception is thrown:

```
com.pczyz.social.domain.user.UserNotExistsException: User [first@example.com] not exists
```























