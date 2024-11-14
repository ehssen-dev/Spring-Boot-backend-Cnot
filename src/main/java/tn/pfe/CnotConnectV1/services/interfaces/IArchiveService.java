package tn.pfe.CnotConnectV1.services.interfaces;

import java.util.List;

import tn.pfe.CnotConnectV1.entities.Archive;

public interface IArchiveService {

	Archive archiveEntities(Long eventId, Long gameId, Long resultId);

	void deleteArchiveById(Long archiveId);

	Archive getArchiveById(Long archiveId);

	List<Archive> getAllArchives();

	Archive saveArchive(Archive archive);

}
