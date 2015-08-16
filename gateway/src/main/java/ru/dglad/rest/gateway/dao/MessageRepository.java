package ru.dglad.rest.gateway.dao;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.dglad.rest.api.requests.SendRequest;
import ru.dglad.rest.api.responses.StatusResponse;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class MessageRepository {

	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final SimpleJdbcInsert insertMessage;

	@Value("${message.default.type.id}")
	private Integer defaultTypeId;

	@Autowired
	public MessageRepository(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		this.insertMessage = new SimpleJdbcInsert(dataSource)
				.withTableName("name of table")
				.withSchemaName("name_of_schema")
				.usingColumns("column_1", "column_2", "...");
		insertMessage.setGeneratedKeyName("msg_id");
	}

	public long insert(SendRequest sendRequest) {
		ImmutableMap.Builder<String, Object> parameters = ImmutableMap.<String,Object>builder()
				.put("sender", sendRequest.getSender())
				.put("address", sendRequest.getAddress())
				.put("request_id", sendRequest.getRequestId());

		if (sendRequest.getTypeId() != null || defaultTypeId != null) {
			Integer typeId = sendRequest.getTypeId();
			parameters.put("message_type_id", (typeId != null) ? typeId : defaultTypeId);
		}

		if (sendRequest.getText() != null) {
			parameters.put("message", sendRequest.getText());
		}

		return insertMessage.executeAndReturnKey(parameters.build()).longValue();
	}

	public StatusResponse getMessageStatus(final long msgId) {
		return jdbcTemplate.queryForObject(
				"select status from some_table where msg_id = :msg_id",
				ImmutableMap.of("msg_id", msgId),
				new RowMapper<StatusResponse>() {
					@Override
					public StatusResponse mapRow(final ResultSet rs, final int rowNum) throws SQLException {
						Integer status = rs.getInt("status");
						if (rs.wasNull()) {
							status = null;
						}
						return new StatusResponse(msgId, status);
					}
				});
	}

	/**
	 *
	 * @param sender sender of the message
	 * @param requestId request id of the message
	 * @return id of this message if it was already sent, otherwise null
	 */
	public String getMessageIdBySender(String sender, String requestId) {
		return jdbcTemplate.queryForObject(
				"select id from some_table where sender = :sender and request_id = :request_id",
				ImmutableMap.of("sender", sender, "request_id", requestId),
				String.class);
	}
}
