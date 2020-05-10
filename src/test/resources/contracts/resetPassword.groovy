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
                        producer("1")
                ),
                "password": regex(".{5,50}")
        )
    }
    response {
        status 200
    }
}
