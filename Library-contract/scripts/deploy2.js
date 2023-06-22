const hre = require("hardhat");

async function main() {

    const [deployer] = await ethers.getSigners();

    console.log(
        "Deploying contracts with the account:",
        deployer.address
    );


    const Library = await hre.ethers.getContractFactory("LibraryContract_updated");
    const library_contract = await Library.deploy();

    await library_contract.deployed();

    console.log(`Contract Address : ${library_contract.address}`);
}

// We recommend this pattern to be able to use async/await everywhere
// and properly handle errors.
main().catch((error) => {
    console.error(error);
    process.exitCode = 1;
});
