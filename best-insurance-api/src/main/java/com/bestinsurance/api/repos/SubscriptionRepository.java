package com.bestinsurance.api.repos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.bestinsurance.api.model.SubscriptionRevenue;
import com.bestinsurance.api.model.Subscription;
import com.bestinsurance.api.model.embedded.SubscriptionId;
public interface SubscriptionRepository extends JpaRepository<Subscription, SubscriptionId> {

    @Query("""
            SELECT new com.bestinsurance.api.model.SubscriptionRevenue(state.name, SUM(sub.paidPrice), COUNT(cust))
                FROM Subscription sub
                JOIN sub.customer cust
                JOIN cust.address addr
                JOIN addr.state state
                GROUP BY state.id""")
    List<SubscriptionRevenue> selectStateSubscriptionRevenue();
}
