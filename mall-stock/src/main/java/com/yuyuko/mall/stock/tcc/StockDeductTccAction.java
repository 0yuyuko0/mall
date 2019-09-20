package com.yuyuko.mall.stock.tcc;

import com.yuyuko.mall.stock.exception.StockDeductRollbackException;
import com.yuyuko.mall.stock.exception.StockNotEnoughException;
import com.yuyuko.mall.stock.param.StockDeductParam;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

import java.util.List;

@LocalTCC
public interface StockDeductTccAction {
    @TwoPhaseBusinessAction(
            name = "stockDeductTccAction",
            commitMethod = "commit",
            rollbackMethod = "rollback")
    boolean prepare(BusinessActionContext actionContext,
                    @BusinessActionContextParameter(paramName = "stockDeductParams") List<StockDeductParam> stockDeductParams,
                    @BusinessActionContextParameter(paramName = "id") long uniqueId) throws StockNotEnoughException, StockDeductRollbackException;

    boolean commit(BusinessActionContext actionContext);

    boolean rollback(BusinessActionContext actionContext);
}
