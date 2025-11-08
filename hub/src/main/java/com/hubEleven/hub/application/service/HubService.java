package com.hubEleven.hub.application.service;

import com.hubEleven.hub.application.command.CreateHubCommand;
import com.hubEleven.hub.application.command.DeleteHubCommand;
import com.hubEleven.hub.application.command.UpdateHubCommand;
import com.hubEleven.hub.application.dto.HubListResult;
import com.hubEleven.hub.application.dto.HubResult;
import java.util.UUID;

public interface HubService {

	HubResult createHub(CreateHubCommand command);

	HubResult updateHub(UpdateHubCommand command);

	void deleteHub(DeleteHubCommand command);

	HubResult getHub(UUID hubId);

	HubListResult getHubs();
}
