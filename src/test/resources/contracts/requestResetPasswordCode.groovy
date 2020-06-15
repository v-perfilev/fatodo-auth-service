package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'request reset password code'
    description 'should return status 200'
    request {
        method POST()
        url("/api/account/request-reset-password-code")
        headers {
            contentType applicationJson()
        }
        body(
                "user": $(
                        consumer(regex(".{5,50}")),
                        producer("test_username_local")
                ),
                "token": anyNonEmptyString()
        )
    }
    response {
        status 200
    }
}
