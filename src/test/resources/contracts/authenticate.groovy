package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'authenticate'
    description 'should return status 200 and header with jwt'
    request {
        method POST()
        url("/api/account/authenticate")
        headers {
            contentType applicationJson()
        }
        body(
                "user": $(
                        consumer(regex(".{5,50}")),
                        producer("local-name")
                ),
                "password": $(
                        consumer(regex(".{5,50}")),
                        producer("test_password")
                ),
                "token": anyNonEmptyString()
        )
    }
    response {
        status 200
        headers {
            header 'Authorization': anyNonBlankString()
        }
    }
}
