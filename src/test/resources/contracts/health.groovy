package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'health'
    description 'should return status 200'
    request {
        method GET()
        url("/api/health")
    }
    response {
        status 200
    }
}
