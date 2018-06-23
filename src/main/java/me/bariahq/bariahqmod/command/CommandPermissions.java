package me.bariahq.bariahqmod.command;

import me.bariahq.bariahqmod.rank.Rank;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandPermissions
{

    Rank level();

    SourceType source();

    boolean blockHostConsole() default false;
}
