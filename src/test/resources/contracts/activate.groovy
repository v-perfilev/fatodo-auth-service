package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'activate'
    description 'should return status 200'
    request {
        method GET()
        url($(
                consumer(regex('\\/api\\/account\\/activate\\/[\\w-\\@\\.]+')),
                producer("/api/account/activate/1")
        ))
    }
    response {
        status 200
    }
}
