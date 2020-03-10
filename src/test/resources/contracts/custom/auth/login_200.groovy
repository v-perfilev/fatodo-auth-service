package contracts.custom.auth

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return 200 and token"
    request {
        method POST()
        url("/authenticate")
        headers {
            contentType applicationJson()
        }
        body('''
            {
              "username":"test_username_1",
              "password":"test_password"
            }
        ''')
    }
    response {
        status 200
        headers {
            header 'Authorization': anyNonBlankString()
        }
    }
}
