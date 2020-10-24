package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'request activation code'
    description 'should return status 200'
    request {
        method GET()
        url($(
                consumer(regex("/api/account/request-activation-code/.+")),
                producer("/api/account/request-activation-code/not-activated-name")
        ))
    }
    response {
        status 200
    }
}
