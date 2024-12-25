package ru.artemmaklashev.telegram_bot_raso.entity.dryMix;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.artemmaklashev.telegram_bot_raso.entity.Product;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dry_mix")
public class DryMix extends Product {
    @ManyToOne
    @JoinColumn(name = "binder_id")
    private Binder binder;

    @ManyToOne
    @JoinColumn(name = "dry_mix_type_id")
    private DryMixType dryMixType;

    @Column(name = "name")
    private String name;
}
