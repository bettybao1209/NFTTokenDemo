using Neo;
using Neo.SmartContract.Framework;

namespace NFTToken
{
    public class TokenState : Nep11TokenState
    {
        public string Description;
        public string tokenURI;
        public UInt256 TxHash;
        public ulong CreatedTime;
        public string Type;
        public UInt160 Creator;
    }
}
