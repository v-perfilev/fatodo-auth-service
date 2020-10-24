package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name 'activate'
    description 'should return status 200'
    request {
        method GET()
        url($(
//                consumer(regex("/api/account/activate/.+")),
                consumer(regex("/api/account/activate/" + uuid().toString())),
                producer("/api/account/activate/34ba7ebf-a43c-4a37-813d-b5a401948857")
        ))
    }
    response {
        status 200
    }
}
