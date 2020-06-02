package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'send reset password code'
    description 'should return status 200'
    request {
        method GET()
        url($(
                consumer(regex('\\/api\\/account\\/request-reset-password-code\\/[\\w-\\@\\.]+')),
                producer("/api/account/request-reset-password-code/test_username_local")
        ))
    }
    response {
        status 200
    }
}
