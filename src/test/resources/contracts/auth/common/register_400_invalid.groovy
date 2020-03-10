package contracts.auth.common

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return 400 cause invalid"
    request {
        method POST()
        url("/register")
        headers {
            contentType applicationJson()
        }
        body(
                "email": "",
                "username": "",
                "password": "test_password"
        )
    }
    response {
        status 400
    }
}
