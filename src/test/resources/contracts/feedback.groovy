package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'feedback'
    description 'register: should return status 200'
    request {
        method POST()
        url("/api/feedback")
        headers {
            contentType applicationJson()
        }
        body(
                "name": anyNonBlankString(),
                "email": anyEmail(),
                "message": anyNonBlankString(),
                "token": anyNonEmptyString()
        )
    }
    response {
        status 200
    }
}
