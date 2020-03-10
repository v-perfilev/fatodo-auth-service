package contracts.auth.common

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return 200"
    request {
        method POST()
        url("/register")
        headers {
            contentType applicationJson()
        }
        body(
                "email": "test_local_create@email.com",
                "username": "test_username_local_create",
                "password": "test_password"
        )
    }
    response {
        status 200
    }
}
