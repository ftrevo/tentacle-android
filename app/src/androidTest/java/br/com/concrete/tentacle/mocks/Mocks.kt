package br.com.concrete.tentacle.mocks

open class Mocks {
    companion object {
        val SESSION_MOCKED = "{\n" +
                "    \"accessToken\": \"\",\n" +
                "    \"refreshToken\": \"\",\n" +
                "    \"tokenType\": \"\"\n" +
                "  }"

        val GAME_REGISTER_MEDIA_ARGUMENT: String = "{\n" +
                "        \"_id\": \"5c3cb881098fab0023f6b0d5\",\n" +
                "        \"title\": \"Space Invaders\",\n" +
                "        \"createdBy\": {\n" +
                "          \"_id\": \"5c1ab12e7c0f4f002272357e\",\n" +
                "          \"name\": \"JOAO DAS NEVES\"\n" +
                "        },\n" +
                "        \"createdAt\": \"2019-01-14T16:27:45.355Z\",\n" +
                "        \"updatedAt\": \"2019-01-14T16:27:45.355Z\"\n" +
                "      }"

        val REGISTER_MEDIA_SUCCESS: String = "{\n" +
                "  \"message\": [\n" +
                "    \"Mídia salva\"\n" +
                "  ],\n" +
                "  \"data\": {\n" +
                "    \"_id\": \"5c4630bccd1df100236a6b2c\",\n" +
                "    \"platform\": \"PS3\",\n" +
                "    \"game\": {\n" +
                "      \"_id\": \"5c3cb881098fab0023f6b0d5\",\n" +
                "      \"title\": \"Battlefield V\"\n" +
                "    },\n" +
                "    \"owner\": {\n" +
                "      \"_id\": \"5c1ab12e7c0f4f002272357e\",\n" +
                "      \"name\": \"JOAO DAS NEVES\"\n" +
                "    },\n" +
                "    \"createdAt\": \"2019-01-21T20:51:08.405Z\",\n" +
                "    \"updatedAt\": \"2019-01-21T20:51:08.405Z\"\n" +
                "  }\n" +
                "}"
    }
}