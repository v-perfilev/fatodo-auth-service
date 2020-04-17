package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'register'
    description 'register: should return status 200'
    request {
        method POST()
        url("/api/register")
        headers {
            contentType applicationJson()
        }
        body(
                "email": anyEmail(),
                "username": regex(".{5,50}"),
                "password": regex(".{5,50}")
        )
    }
    response {
        status 200
        headers {
            contentType applicationJson()
        }
    }
}
