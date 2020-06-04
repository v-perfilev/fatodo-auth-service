package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'request activation code'
    description 'should return status 200'
    request {
        method GET()
        url($(
                consumer(regex('\\/api\\/account\\/request-activation-code\\/[\\w-\\@\\.]+')),
                producer("/api/account/request-activation-code/test_username_not_activated")
        ))
    }
    response {
        status 200
    }
}
