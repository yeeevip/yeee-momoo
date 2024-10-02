package vip.yeee.memoo.demo.blockchain.hyperchain.biz.bo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class NftIssueBO {
    private String tokenId;
    private BlockchainAccountBO accountBO;
}
