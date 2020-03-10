package contracts.auth.common

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return 401 cause invalid"
    request {
        method POST()
        url("/authenticate")
        headers {
            contentType applicationJson()
        }
        body(
                "username": "",
                "password": ""
        )
    }
    response {
        status 400
    }
}
