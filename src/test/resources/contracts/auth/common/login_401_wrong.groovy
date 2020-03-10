package contracts.auth.common

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return 401 cause wrong password"
    request {
        method POST()
        url("/authenticate")
        headers {
            contentType applicationJson()
        }
        body(
                "username": "test_username_local",
                "password": "wrong_password"
        )
    }
    response {
        status 401
    }
}
