package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'reset password'
    description 'should return status 200'
    request {
        method POST()
        url("/api/account/reset-password")
        headers {
            contentType applicationJson()
        }
        body(
                "code": $(
                        consumer(anyNonBlankString()),
                        producer("34ba7ebf-a43c-4a37-813d-b5a401948857")
                ),
                "password": regex(".{5,50}"),
                "token": anyNonEmptyString()
        )
    }
    response {
        status 200
    }
}
