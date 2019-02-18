package com.example.dmitrykostin.revolut_client.revolut_api.response

import com.squareup.moshi.Json

data class ConfirmResponse(
    @Json(name = "user") val user: User,
    @Json(name = "wallet") val wallet: Wallet,
    @Json(name = "accessToken") val accessToken: String
)


/**
{
"user": {
"id": "2802c2ad-23a",
"createdDate": 1492596720266,
"address": {
"city": "Richmond",
"country": "GB",
"postcode": "TW9 ",
"region": "England",
"streetLine1": ""
},
"birthDate": [1990, 12, 16],
"firstName": "D",
"lastName": "K",
"phone": "+44blabla",
"email": "...@gmail.com",
"emailVerified": false,
"locked": false,
"state": "ACTIVE",
"referralCode": "dmitripos",
"underReview": false,
"riskAssessed": false,
"kyc": "PASSED"
},
"wallet": {
"id": "40504cb2-121e-4986-b9f7-",
"ref": "34678580",
"state": "ACTIVE",
"baseCurrency": "GBP",
"topupLimit": 2500000,
"totalTopup": 42500,
"pockets": [{
"id": "511ee4ad-2d74-41d1-8c50-31af4e98030a",
"state": "ACTIVE",
"currency": "GBP",
"balance": 10294,
"blockedAmount": 0
}, {
"id": "c34a67b5-8888-4515-9490-416478a37618",
"state": "ACTIVE",
"currency": "EUR",
"balance": 0,
"blockedAmount": 0
}, {
"id": "0f638809-ed35-4929-9c1a-37c99ebbe1d0",
"state": "ACTIVE",
"currency": "USD",
"balance": 0,
"blockedAmount": 0
}]
},
"accessToken": "TOKEN"
}
 */