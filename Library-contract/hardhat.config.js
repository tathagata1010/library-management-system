require("@nomicfoundation/hardhat-toolbox");
require("hardhat-gas-reporter");
require('solidity-coverage')
require('dotenv').config()
/** @type import('hardhat/config').HardhatUserConfig */



module.exports = {
  solidity: "0.8.18",
  gasReporter: {
    enabled: true
  },
  networks: {
    sepolia: {
      url: "https://sepolia.infura.io/v3/4a560757ab1b4a1287a35c89055cc388",
      accounts: [`${process.env.PRIVATE_KEY}`]

    }
  }

};
