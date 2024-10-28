db = sb.getSiblingDB('ORCHESTRADOR_ADM')

db.createUser(
    {
        user: "orchUser",
        pwd: "orchPwd",
        roles:[
            {
                role: "readWrite",
                db: "ORCHESTRADOR_ADM"
            }
        ]
    }
)

db.createCollection('infoGithub');