package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'send activation code'
    description 'should return status 200'
    request {
        method GET()
        url($(
                consumer(regex('\\/api\\/account\\/send-activation-code\\/[\\w-\\@\\.]+')),
                producer("/api/account/send-activation-code/test_username_local")
        ))
    }
    response {
        status 200
    }
}
