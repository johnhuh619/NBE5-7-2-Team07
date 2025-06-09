package com.luckyseven.backend.domain.budget.validator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.luckyseven.backend.domain.budget.dao.BudgetRepository;
import com.luckyseven.backend.domain.budget.dto.BudgetUpdateRequest;
import com.luckyseven.backend.domain.budget.entity.Budget;
import com.luckyseven.backend.domain.budget.entity.CurrencyCode;
import com.luckyseven.backend.sharedkernel.exception.CustomLogicException;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BudgetValidatorTests {

  @InjectMocks
  private BudgetValidator budgetValidator;
  @Mock
  private BudgetRepository budgetRepository;

  @Test
  @DisplayName("validateBudgetNotExist는 예산이 이미 존재하면 예외가 발생한다")
  void validateBudgetNotExist_throw_exception_when_budget_already_exists() throws Exception {

    // given
    Long teamId = 1L;
    Budget budget = Budget.builder()
        .totalAmount(BigDecimal.valueOf(100000))
        .setBy(1L)
        .balance(BigDecimal.valueOf(100000))
        .foreignCurrency(CurrencyCode.USD)
        .foreignBalance(BigDecimal.valueOf(71.75))
        .avgExchangeRate(BigDecimal.valueOf(1393.7))
        .build();

    when(budgetRepository.findByTeamId(teamId)).thenReturn(Optional.of(budget));

    // when & then
    assertThatThrownBy(
        () -> {
          budgetValidator.validateBudgetNotExist(teamId);
        }
    ).isInstanceOf(CustomLogicException.class);

  }

  @Test
  @DisplayName("validateBudgetExist는 예산이 존재하지 않으면 오류가 발생한다")
  void validateBudgetExist_throw_exception_when_budget_does_not_exist() throws Exception {

    // given
    Long teamId = 1L;
    when(budgetRepository.findByTeamId(teamId)).thenReturn(Optional.empty());

    // when & then
    assertThatThrownBy(
        () -> {
          budgetValidator.validateBudgetExist(teamId);
        }
    ).isInstanceOf(CustomLogicException.class);

  }

  @Test
  @DisplayName("validateRequest(BudgetUpdateRequest)는 isExchanged가 null이어도 예외를 발생시키지 않는다")
  void validateRequest_update_request_with_null_isExchanged_should_not_throw() {
    BudgetUpdateRequest request = new BudgetUpdateRequest(
        BigDecimal.valueOf(1000),
        null,
        null,
        null
    );

    assertDoesNotThrow(() -> budgetValidator.validateRequest(request));

  }
}
