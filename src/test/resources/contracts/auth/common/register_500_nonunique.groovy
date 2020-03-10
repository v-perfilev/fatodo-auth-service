package contracts.auth.common

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return 500 cause nonunique"
    request {
        method POST()
        url("/register")
        headers {
            contentType applicationJson()
        }
        body(
                "email": "test_local@email.com",
                "username": "test_username_local",
                "password": "test_password"
        )
    }
    response {
        status 500
    }
}
