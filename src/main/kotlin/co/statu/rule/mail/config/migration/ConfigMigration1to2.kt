package co.statu.rule.mail.config.migration

import co.statu.parsek.annotation.Migration
import co.statu.parsek.api.config.PluginConfigMigration
import io.vertx.core.json.JsonObject

@Migration
class ConfigMigration1to2 : PluginConfigMigration(1, 2, "Add template file prefix") {
    override fun migrate(config: JsonObject) {
        config.put("templateFilePrefix", "../mail/")
    }
}