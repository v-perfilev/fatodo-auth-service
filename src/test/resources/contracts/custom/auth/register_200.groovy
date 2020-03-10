package contracts.custom.auth

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return 200"
    request {
        method POST()
        url("/register")
        headers {
            contentType applicationJson()
        }
        body('''
            {
              "email":"test_2@email.com",
              "username":"test_username_2",
              "password":"test_password"
            }
        ''')
    }
    response {
        status 200
    }
}
