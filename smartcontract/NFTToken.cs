using System.ComponentModel;
using System.Numerics;
using Neo.SmartContract.Framework;
using Neo.SmartContract.Framework.Native;
using Neo.SmartContract.Framework.Services;

namespace NFTToken
{
    [DisplayName("NFTToken")]
    [ManifestExtra("Description", "This is a NFTToken")]
    public sealed class NFTToken : MyNep11Token<TokenState>
    {
        // TBD
        [Safe]
        public override string Symbol() => "NFT";

        [Safe]
        public override Map<string, object> Properties(ByteString tokenId)
        {
            StorageMap tokenMap = new(Storage.CurrentContext, Prefix_Token);
            TokenState token = (TokenState)StdLib.Deserialize(tokenMap[tokenId]);
            Map<string, object> map = base.Properties(tokenId);
            map["desc"] = token.Description;
            map["name"] = token.Name;
            map["uri"] = token.tokenURI;
            map["owner"] = token.Owner;
            map["createdTime"] = token.CreatedTime;
            return map;
        }

        public static ByteString Mint(string data)
        {
            Map<string, string> customData = (Map<string, string>)StdLib.JsonDeserialize(data);
            Transaction tx = (Transaction)Runtime.ScriptContainer;
            ByteString tokenId = NewTokenId();
            Mint(tokenId, new TokenState
            {
                Name = customData["name"],
                Owner = tx.Sender,
                tokenURI = customData["uri"],
                Description = customData["desc"],
                CreatedTime = Runtime.Time
            });
            return tokenId;
        }

        public static ByteString[] MintInBatch(string data, BigInteger amount)
        {
            List<ByteString> tokenIds = new();
            for (int i = 0; i < amount; i++)
            {
                tokenIds.Add(Mint(data));
            }
            return tokenIds;
        }

        public static int test()
        {
            return 1;
        }
    }
}
