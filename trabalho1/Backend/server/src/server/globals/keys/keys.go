package keys

import "server/keypair"

const ServerKeypairPath = "../secrets/keypair.pem"
const ServerPublicKeyPath = "../secrets/publicKey.pem"

var Server *keypair.Keypair
