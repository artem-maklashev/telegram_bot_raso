package ru.artemmaklashev.telegram_bot_raso.entity.gypsum;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.artemmaklashev.telegram_bot_raso.entity.Product;

@EqualsAndHashCode(callSuper = true)
@Data
//@AllArgsConstructor
//@NoArgsConstructor
@Entity
@Table
public class Gypsum extends Product {

}
