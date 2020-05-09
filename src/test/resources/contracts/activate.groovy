package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'activate'
    description 'should return status 200'
    request {
        method GET()
        url($(
                consumer(regex('\\/api\\/activation\\/activate\\/[\\w-\\@\\.]+')),
                producer("/api/activation/activate/1")
        ))
    }
    response {
        status 200
    }
}
