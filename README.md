# NFTTokenDemo

This is a web project for nft token based on neow3j and neo blockchain.

# Database schema
You can find the sql script in the `DB_SQL.sql` file under the resources directory. It creates two tables for run the demo.

# Scheduled task
Here we create a scheduled task to sync the block and get all the `transfer` event for our nft token contract. Then we store the transactions into the mysql db for query, such as `query all the trades for specified tokenId`
or `query all the transactions for specified address`.

# Contract methods
We provides several examples to invoke methods defined in the contract, such as `mint`, `properties`, `transfer`. For other methods, we like invoke them likewise.
