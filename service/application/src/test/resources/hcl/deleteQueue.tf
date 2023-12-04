terraform {
  required_providers {
    solacebroker = {
      source = "registry.terraform.io/solaceproducts/solacebroker"
    }
  }
}

provider "solacebroker" {
  alias    = "fh71c08said"
  username = "${var.username}"
  password = "${var.password}"
  url      = "${var.url}"
}

data "solacebroker_msg_vpn" "fh71c08said" {
  provider     = solacebroker.fh71c08said
  msg_vpn_name = "default"
}

variable username {
}

variable password {
}

variable url {
}